package app.models.access

import java.time.Instant

import app.models.quiz.QuizState
import app.models.quiz.QuizState.TimerState
import app.models.quiz.Team
import hydro.common.CollectionUtils
import hydro.common.GuavaReplacement.ImmutableBiMap
import hydro.common.ScalaUtils
import hydro.models.modification.EntityType
import hydro.models.Entity
import hydro.models.access.ModelField
import hydro.models.access.ModelField.IdModelField

import scala.collection.immutable.Seq

object ModelFields {
  // **************** Methods **************** //
  def id[E <: Entity](implicit entityType: EntityType[E]): ModelField[Long, E] = entityType match {
    case app.models.quiz.Team.Type      => Team.id.asInstanceOf[ModelField[Long, E]]
    case app.models.quiz.QuizState.Type => QuizState.id.asInstanceOf[ModelField[Long, E]]
  }

  // **************** Enumeration of all fields **************** //
  object Team {
    private type E = Team

    case object id extends IdModelField[E]
    case object name extends ModelField[String, E]("name", _.name, v => _.copy(name = v))
    case object score extends ModelField[Int, E]("score", _.score, v => _.copy(score = v))
    case object index extends ModelField[Int, E]("index", _.index, v => _.copy(index = v))
  }

  object QuizState {
    private type E = QuizState

    case object id extends IdModelField[E]
    case object roundIndex extends ModelField[Int, E]("roundIndex", _.roundIndex, v => _.copy(roundIndex = v))
    case object questionIndex
        extends ModelField[Int, E]("questionIndex", _.questionIndex, v => _.copy(questionIndex = v))
    case object questionProgressIndex
        extends ModelField[Int, E](
          "questionProgressIndex",
          _.questionProgressIndex,
          v => _.copy(questionProgressIndex = v))
    case object timerState
        extends ModelField[TimerState, E]("timerState", _.timerState, v => _.copy(timerState = v))
  }

  // **************** Field numbers **************** //
  private val fieldToNumberMap: ImmutableBiMap[ModelField.any, Int] =
    CollectionUtils.toBiMapWithStableIntKeys(
      stableNameMapper = field =>
        ScalaUtils.stripRequiredPrefix(field.getClass.getName, prefix = ModelFields.getClass.getName),
      values = Seq(
        Team.id,
        Team.name,
        Team.score,
        Team.index,
        QuizState.id,
        QuizState.roundIndex,
        QuizState.questionIndex,
        QuizState.questionProgressIndex,
        QuizState.timerState,
      )
    )
  def toNumber(field: ModelField.any): Int = fieldToNumberMap.get(field)
  def fromNumber(number: Int): ModelField.any = fieldToNumberMap.inverse().get(number)
}
